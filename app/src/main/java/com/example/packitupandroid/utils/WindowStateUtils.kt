package com.example.packitupandroid.utils


/**
 * Enum class representing different types of navigation in the application.
 */
enum class PackItUpNavigationType {
    /**
     * Bottom navigation type commonly used in mobile applications.
     */
    BOTTOM_NAVIGATION,

    /**
     * Navigation rail type typically used in tablet or desktop applications.
     */
    NAVIGATION_RAIL,

    /**
     * Permanent navigation drawer type used for persistent navigation options.
     */
    PERMANENT_NAVIGATION_DRAWER,
}

/**
 * Enum class representing different content layouts for the application.
 */
enum class PackItUpContentType {
    /**
     * Layout displaying only a list of items.
     */
    LIST_ONLY, // default

    /**
     * Layout displaying both a list of items and details of the selected item.
     * Typically used in expanded screens like tablets or desktops.
     */
    LIST_AND_DETAIL, // expanded screen
}
