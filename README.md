# Snack Bar Wrapper

[![](https://jitpack.io/v/ambishiva/Snack-Bar-Reusable-Component.svg)](https://jitpack.io/#ambishiva/Snack-Bar-Reusable-Component)

### This library can be used/intergrate in a project as a wrapper for alert messages so that if required to change snackbar to any other lib then can be changed easily without affecting or changes on UI layer.
&nbsp;
### Dev can use this lib as a bridge between Google snackbar and their application. Flexible to adobt new changes from Snackbar library so that core dev team should focus on delivering the things instead of thinking about library updation or learn/adopt new changes.

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





