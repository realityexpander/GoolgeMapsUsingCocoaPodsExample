package data

import data.loadMarkers.MarkersResult
import kotlinx.coroutines.flow.MutableStateFlow
import maps.Marker
import maps.MarkerIdStr
import co.touchlab.kermit.Logger as Log

open class MarkersRepo(
    private val appSettings: AppSettings,
    val updateMarkersResultFlow: MutableStateFlow<MarkersResult> = MutableStateFlow(appSettings.markersResult),
    private var markersResult: MarkersResult = appSettings.markersResult
) {
    init {
       Log.d { "MarkersRepo: init, instance=$this" }
       updateMarkersResult(appSettings.markersResult)
    }

    // Completely replaces the current MarkersResult with a new value
    private fun updateMarkersResult(newMarkersResult: MarkersResult) {
        appSettings.markersResult = newMarkersResult
        this.markersResult = appSettings.markersResult
        updateMarkersResultFlow.tryEmit(appSettings.markersResult)
    }

    fun markersResult() = markersResult

    fun addMarker(marker: Marker): MarkersResult {
        // todo should update if expired? - Add functionality in caller instead
        this.marker(marker.id)?.run {
            // Log.w("MarkerRepo: addMarker: marker already exists, id: ${marker.id}, add ignored.")
            updateMarkersResult(appSettings.markersResult) // trigger update to flow anyway // todo test that this is correct
            return markersResult()
        }

        updateMarkersResult(
            appSettings.markersResult.copy(
                markerIdToMarker =
                    appSettings.markersResult.markerIdToMarker +
                        (marker.id to marker)
            )
        )

        return markersResult()
    }

    fun removeMarker(id: MarkerIdStr): MarkersResult {
        Log.i("MarkerRepo: removeMarker: id=$id")
        this.marker(id) ?: run {
            updateMarkersResult(appSettings.markersResult) // trigger update to flow anyway // todo test that this is correct
            return markersResult()
        }

        updateMarkersResult(
            appSettings.markersResult.copy(
                markerIdToMarker =
                    appSettings.markersResult.markerIdToMarker - id
                )
        )

        return markersResult()
    }

    fun marker(id: MarkerIdStr): Marker? {
        return appSettings.markersResult.markerIdToMarker[id]
    }

    fun markers(): List<Marker> {
        return appSettings.markersResult.markerIdToMarker.values.toList()
    }

    fun clearAllMarkers(): MarkersResult {
        Log.i("MarkerRepo: clearAllMarkers")
        updateMarkersResult(
            appSettings.markersResult.copy(
                markerIdToMarker = emptyMap()
            )
        )

        return markersResult()
    }

    // Note: Blows away all previous data.  Use with caution.
    fun replaceMarker(replacementMarker: Marker): MarkersResult {
        Log.i("MarkerRepo: updateAllDataForMarker: replacementMarker.id=${replacementMarker.id}")
        updateMarkersResult(
            appSettings.markersResult.copy(
                markerIdToMarker =
                    appSettings.markersResult.markerIdToMarker +
                        (replacementMarker.id to replacementMarker)
            )
        )

        return markersResult()
    }

    fun updateMarkerIsSeen(markerToUpdate: Marker, isSeen: Boolean): MarkersResult {
        Log.i("MarkerRepo: updateMarkerIsSeen: markerToUpdate.id=${markerToUpdate.id}, isSeen=$isSeen")
        val originalMarker =
            this.marker(markerToUpdate.id)
                ?: run {
                    Log.w("MarkerRepo: updateMarkerIsSeen: marker not found, id: ${markerToUpdate.id}")
                    return appSettings.markersResult
                }
        if(originalMarker.isSeen == isSeen) { // no change
            updateMarkersResult(appSettings.markersResult)
            return appSettings.markersResult
        }

        updateMarkersResult(
            appSettings.markersResult.copy(
                markerIdToMarker =
                    appSettings.markersResult.markerIdToMarker +
                        (originalMarker.id to originalMarker.copy(
                            isSeen = isSeen
                        ))
            )
        )

        return markersResult()
    }

    fun updateMarkerDetails(markerWithUpdatedDetails: Marker): MarkersResult {
        Log.i("MarkerRepo: updateMarkerDetails: markerWithUpdatedDetails.id=${markerWithUpdatedDetails.id}")
        // todo - should add marker if not found? Add upsert function?
        val originalMarker =
            this.marker(markerWithUpdatedDetails.id)
                ?: run {
                    Log.w("MarkerRepo: updateMarkerDetails: marker not found, id: ${markerWithUpdatedDetails.id}")
                    return appSettings.markersResult
                }

        updateMarkersResult(
            appSettings.markersResult.copy(
                markerIdToMarker =
                    appSettings.markersResult.markerIdToMarker +
                        (originalMarker.id to originalMarker.copy(
                            isDetailsLoaded = true, // force to indicate details have been loaded
                            markerDetailsPageUrl = markerWithUpdatedDetails.markerDetailsPageUrl,
                            mainPhotoUrl = markerWithUpdatedDetails.mainPhotoUrl,
                            markerPhotos = markerWithUpdatedDetails.markerPhotos,
                            photoCaptions = markerWithUpdatedDetails.photoCaptions,
                            photoAttributions = markerWithUpdatedDetails.photoAttributions,
                            inscription = markerWithUpdatedDetails.inscription,
                            englishInscription = markerWithUpdatedDetails.englishInscription,
                            spanishInscription = markerWithUpdatedDetails.spanishInscription,
                            erected = markerWithUpdatedDetails.erected,
                            credits = markerWithUpdatedDetails.credits,
                            location = markerWithUpdatedDetails.location,
                            markerPhotos2 = markerWithUpdatedDetails.markerPhotos2,
                            lastUpdatedDetailsEpochSeconds = markerWithUpdatedDetails.lastUpdatedDetailsEpochSeconds
                        ))
            )

        )

        return markersResult()
    }

    fun upsertMarkerDetails(marker: Marker): MarkersResult {
        Log.i("MarkerRepo: upsertMarkerDetails: marker.id=${marker.id}")
        marker(marker.id)?.let {
            updateMarkerDetails(marker)
        } ?: run {
            addMarker(marker)
        }

        return markersResult()
    }

    fun updateMarkerBasicInfo(markerWithUpdatedBasicInfo: Marker): MarkersResult {
        Log.i("MarkerRepo: updateMarkerBasicInfo: markerWithUpdatedBasicInfo.id=${markerWithUpdatedBasicInfo.id}")
        // todo - should add marker if not found? Add upsert function?
        val originalMarker =
            marker(markerWithUpdatedBasicInfo.id)
                ?: run {
                    Log.w("MarkerRepo: updateMarkerBasicInfo: marker not found, id: ${markerWithUpdatedBasicInfo.id}")
                    return appSettings.markersResult
                }

        updateMarkersResult(
            appSettings.markersResult.copy(
                markerIdToMarker =
                    appSettings.markersResult.markerIdToMarker +
                        (originalMarker.id to originalMarker.copy(
                            position = markerWithUpdatedBasicInfo.position,
                            title = markerWithUpdatedBasicInfo.title,
                            subtitle = markerWithUpdatedBasicInfo.subtitle,
                            alpha = markerWithUpdatedBasicInfo.alpha
                        ))
            )
        )

        return markersResult()
    }

    fun upsertMarkerBasicInfo(marker: Marker): MarkersResult {
        Log.i("MarkerRepo: upsertMarkerBasicInfo: marker.id=${marker.id}")
        marker(marker.id)?.let {
            updateMarkerBasicInfo(marker)
        } ?: run {
            addMarker(marker)
        }

        return markersResult()
    }

    fun setIsParseMarkersPageFinished(isFinished: Boolean): MarkersResult {
        Log.i("MarkerRepo: setIsParseMarkersPageFinished: isFinished=$isFinished")
        updateMarkersResult(
            appSettings.markersResult.copy(
                isParseMarkersPageFinished = isFinished
            )
        )

        return markersResult()
    }
}