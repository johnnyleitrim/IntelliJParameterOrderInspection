# Method Parameter Order Inspection
<img align="left" src="src/main/resources/META-INF/pluginIcon.svg" alt="Plugin Logo" width="64" height="64">
An IntelliJ plugin that adds a Java code inspection to check if parameters are passed to methods in the correct order.

[IntelliJ plugin page](https://plugins.jetbrains.com/plugin/15354-method-parameter-order-inspection)

---

[![Build Status](https://travis-ci.org/johnnyleitrim/IntelliJParameterOrderInspection.svg?branch=master)](https://travis-ci.org/johnnyleitrim/IntelliJParameterOrderInspection)

# What is this?

Arguments that are passed into a method call in the wrong order can lead to hard-to-spot bugs in code.  When these method parameters are of the same type, the compiler will not warn you that you have passed them in the wrong order, and no static analysis tool currently highlight this issue.

This IntelliJ plugin adds a new code inspection under  **Java > Probable Bugs**:  **Parameters should be passed in the correct order**

Running a code analysis on you project with this inspection enabled will highlight places where you may be passing parameters in the wrong order.

## Example
You have a method:
```java
public void insert(String firstName, String lastName) {
  db.savePerson(firstName, lastName);
}
```

However,  in one part of your code, you have accidentally passed the arguments in the wrong order:
```
insert(lastName, firstName);
```

The compier will not complain, because both arguments are the same type: `String`.
Running a code analysis with this plugin installed will warn that you are passing arguments in the wrong order.

# How do I use it?

## Install the plugin
In IntelliJ, go to **Preferences > Plugins**, and search for **Method Parameter Order Inspection**.  Click on install and restart the IDE.

## Run a code analysis
- Next, right click on the project you want to analyse, and select **Analyse > Inspect Code...**.
- Click on the 3 dot menu under the **Inspection profile** section.
- Search for "Parameters should be passed in the correct order", and ensure the checkbox beside it is ticked.
- Dismiss the "Inspections" dialog, and run the inspection by clicking "OK".

# Running locally
If you would rather try this plugin without first installing it in your IntelliJ IDE, you can download this code and run:
```bash
./gradlew runIde
```

This will start an IntelliJ Community Edition IDE where you can try out the plugin without changing your standard IntelliJ intall.

# Icons
Plugin icon made by [Freepik](https://www.flaticon.com/authors/freepik) from [Flaticon](https://www.flaticon.com/)
