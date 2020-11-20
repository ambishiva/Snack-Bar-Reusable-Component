# Snack Bar Wrapper

[![](https://jitpack.io/v/ambishiva/Snack-Bar-Reusable-Component.svg)](https://jitpack.io/#ambishiva/Snack-Bar-Reusable-Component)

### This library is used to do changes in the snack bar as per the requirment for example change the backgroud color of snack bar , text color changes, show success view in case of success as well to change the font in snackbar

&nbsp;
### How to use
&nbsp;
##### Snack Bar Message
```Java
Alert.with(context, "Snack bar message").show()
```
&nbsp;
##### Change background color
```Java
Alert.with(this, "Snackbar with red background")
            .backgroundColor(android.R.color.holo_red_dark).show()
```
&nbsp;
##### Change text color
```Java
Alert.with(this, "Snackbar with red text color")
            .textColor(android.R.color.holo_red_dark).show()
```
&nbsp;
##### Change snack bar duration
```java
Alert.with(this, "Snackbar with 20 seconds")
            .textColor(android.R.color.holo_red_dark)
            .duration(20000).show()
```

&nbsp;
##### Snack bar with font
```java
 val hindiFont = Typeface.createFromAsset(assets, "hind_font.ttf")
        val snackBarBuilder = Alert.with(this, "Snackbar with hindi font")
            .textColor(android.R.color.holo_red_dark)
            .font(hindiFont).show()
```

&nbsp;
##### Snack bar with error view
```java
Alert.with(this, "Snackbar Error Message")
            .textColor(R.color.white)
            .error().show()
```

&nbsp;
##### Snack bar with success view
```java
Alert.with(this, "Snackbar success Message")
            .textColor(R.color.white)
            .success().show()
```





