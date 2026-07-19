# Home Screen UI Redesign

Redesign the Home Screen to match the provided reference image, including a new header, "Trending books" carousel, "Swap your old books" banner, and "Join a book club" section. The navigation bar will remain as it was but with updated colors to match the new theme.

## User Review Required

- **Data Source for Trending/Book Clubs**: Since these aren't currently in the local database, I will use placeholder data for now.
- **Color Accuracy**: I have extracted hex codes from the image to ensure the "exact color code" requirement is met.

## Proposed Changes

### Theme & Colors

#### [Color.kt](file:///C:/Users/nandan/Desktop/projects/readr/app/src/main/java/com/readr/app/ui/theme/Color.kt)

- Add new colors extracted from the image:
    - `BackgroundWhite` = `#F8F9FB`
    - `TextBlack` = `#1A1A1A`
    - `TextGrey` = `#8E8E93`
    - `BannerPurple` = `#E8EAF6`
    - `BannerTextPurple` = `#5C6BC0`
    - `AccentBlue` = `#4A90E2`

#### [Theme.kt](file:///C:/Users/nandan/Desktop/projects/readr/app/src/main/java/com/readr/app/ui/theme/Theme.kt)

- Update `LightColorScheme` to use the new background and text colors.

---

### Components

#### [ReadrBottomNavBar.kt](file:///C:/Users/nandan/Desktop/projects/readr/app/src/main/java/com/readr/app/ui/components/ReadrBottomNavBar.kt)

- Update the navigation bar colors to match the new light theme (White background, Grey icons, Blue/Black selected state).

---

### Home Screen

#### [HomeScreen.kt](file:///C:/Users/nandan/Desktop/projects/readr/app/src/main/java/com/readr/app/ui/screens/HomeScreen.kt)

- **Header**: "Welcome back" title with "Discover Your Next Great Read" subtitle. Notification bell icon on the right.
- **Trending Books**: Horizontal list of books with specific styling (larger covers, bold titles).
- **Swap Banner**: A light purple card with "Swap your old books" text and a "Swap now →" button.
- **Book Club Section**: "Join a book club" header with "Recommended Book Clubs for You" subtitle and a horizontal list of club avatars.

## Verification Plan

### Automated Tests
- Run `:app:assembleDebug` to ensure no compilation errors.

### Manual Verification
- Deploy to device and compare with the reference image.
- Verify that the bottom navigation bar still works and navigates to other screens.
