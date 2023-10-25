package data

import Location
import com.russhwolf.settings.Settings
import kotlinx.serialization.encodeToString
import data.loadMarkers.MarkersResult
import json
import co.touchlab.kermit.Logger as Log

// Settings Keys
const val kCachedMarkersResultSetting =                    "cachedMarkersResult"
const val kCachedMarkersLastUpdatedEpochSecondsSetting =   "cachedMarkersLastUpdatedEpochSeconds"
const val kCachedMarkersLastLoadLocationSetting =          "cachedMarkersLastLocation"
const val kLastKnownUserLocationSetting =                  "lastKnownUserLocation"
const val kStartBackgroundUpdatesWhenAppLaunchesSetting =  "startBackgroundUpdatesWhenAppLaunches"
const val kTalkRadiusMilesSetting =                        "talkRadiusMiles"
const val kIsMarkersLastUpdatedLocationVisibleSetting =    "isMarkersLastUpdatedLocationVisible"
const val kIsRecentlySeenMarkersPanelVisibleSetting =      "isRecentlySeenMarkersPanelVisible"
const val kIsPermissionsGranted =                          "isPermissionsGranted"

fun Settings.printAppSettings() {
    // Show current settings
    Log.d { "keys from settings: $keys" }
    Log.d("Settings: cachedMarkersResult mapMarkers.size= " +
            cachedMarkersResult().markerIdToMapMarker.size.toString())
    Log.d("Settings: cachedMarkersLastUpdatedEpochSeconds= " +
            getLong(kCachedMarkersLastUpdatedEpochSecondsSetting, 0L).toString())
    Log.d("Settings: cachedMarkersLastLocation= " +
            getString(kCachedMarkersLastLoadLocationSetting, "{latitude:0.0, longitude:0.0}"))
    Log.d("Settings: LastKnownUserLocation= " +
            getString(kLastKnownUserLocationSetting, "{latitude:0.0, longitude:0.0}"))
    Log.d("Settings: startBackgroundUpdatesWhenAppLaunches= " +
            getBoolean(kStartBackgroundUpdatesWhenAppLaunchesSetting, false).toString())
    Log.d("Settings: talkRadiusMiles= " +
            getDouble(kTalkRadiusMilesSetting, 0.5).toString())
    Log.d("Settings: isMarkersLastUpdatedLocationVisible= " +
            getBoolean(kIsMarkersLastUpdatedLocationVisibleSetting, false).toString())
    Log.d("Settings: isRecentlySeenMarkersPanelVisible= " +
            getBoolean(kIsRecentlySeenMarkersPanelVisibleSetting, false).toString())
}

// Cached Markers
fun Settings.setCachedMarkersResult(markersResult: MarkersResult) {
    putString(kCachedMarkersResultSetting, json.encodeToString(markersResult))
}
fun Settings.cachedMarkersResult(): MarkersResult {
    return json.decodeFromString(getString(kCachedMarkersResultSetting, "{}"))
}

// Cached Markers Last Updated
fun Settings.setCachedMarkersLastUpdatedEpochSeconds(epochSeconds: Long) {
    putLong(kCachedMarkersLastUpdatedEpochSecondsSetting, epochSeconds)
}
fun Settings.cachedMarkersLastUpdatedEpochSeconds(): Long {
    return getLong(kCachedMarkersLastUpdatedEpochSecondsSetting, 0L)
}

// Cached Markers Last Loaded Location
fun Settings.setCachedMarkersLastUpdatedLocation(location: Location) {
    putString(kCachedMarkersLastLoadLocationSetting, json.encodeToString(location))
}
fun Settings.cachedMarkersLastUpdatedLocation(): Location {
    return json.decodeFromString(getString(kCachedMarkersLastLoadLocationSetting, "{latitude:0.0, longitude:0.0}"))
}

// Last Known User Location
fun Settings.setLastKnownUserLocation(location: Location) {
    putString(kLastKnownUserLocationSetting, json.encodeToString(location))
}
fun Settings.lastKnownUserLocation(): Location {
    return json.decodeFromString(getString(kLastKnownUserLocationSetting, "{latitude:0.0, longitude:0.0}"))
}

// • For Settings panel
fun Settings.setShouldAutomaticallyStartTrackingWhenAppLaunches(shouldStartBackgroundUpdatesWhenAppLaunches: Boolean) {
    putBoolean(kStartBackgroundUpdatesWhenAppLaunchesSetting, shouldStartBackgroundUpdatesWhenAppLaunches)
}
fun Settings.shouldAutomaticallyStartTrackingWhenAppLaunches(): Boolean {
    return getBoolean(kStartBackgroundUpdatesWhenAppLaunchesSetting, false)
}

fun Settings.setTalkRadiusMiles(talkRadiusMiles: Double) {
    putDouble(kTalkRadiusMilesSetting, talkRadiusMiles)
}
fun Settings.talkRadiusMiles(): Double {
    return getDouble(kTalkRadiusMilesSetting, 0.5)
}

fun Settings.isMarkersLastUpdatedLocationVisible(): Boolean {
    return getBoolean(kIsMarkersLastUpdatedLocationVisibleSetting, false)
}
fun Settings.setIsMarkersLastUpdatedLocationVisible(isMarkersLastUpdatedLocationVisible: Boolean) {
    putBoolean(kIsMarkersLastUpdatedLocationVisibleSetting, isMarkersLastUpdatedLocationVisible)
}

fun Settings.isRecentlySeenMarkersPanelVisible(): Boolean {
    return getBoolean(kIsRecentlySeenMarkersPanelVisibleSetting, false)
}
fun Settings.setIsRecentlySeenMarkersPanelVisible(isRecentlySeenMarkersPanelVisible: Boolean) {
    putBoolean(kIsRecentlySeenMarkersPanelVisibleSetting, isRecentlySeenMarkersPanelVisible)
}

fun Settings.isPermissionsGranted(): Boolean {
    return getBoolean(kIsPermissionsGranted, false)
}
fun Settings.setIsPermissionsGranted(isPermissionsGranted: Boolean) {
    putBoolean(kIsPermissionsGranted, isPermissionsGranted)
}
