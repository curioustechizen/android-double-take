###Adding more Animations:

If you want to contribute more animations to this library, the general procedure would be

  - Add a class for your animation in the library project
  - Add an `Activity` for your animation in the sample project
  - Add resources and other classes for the sample in the sample project
  - Edit `MainActivity.java` in the sample project and add your sample to the list you see there (`mSamples`)
  - Add your sample `Activity` to `AndroidManifest.xml`

With all these in place, send a pull request.

####Resources in the library:

Neither of the currently provided animations requires any resources in the library project. However, other animations might require the use of resources. This is why I have consciously chosen to make this an Android library project. Do not hesitate to add resources in the library if you deem fit.


###Help Resolving issues:

Any help in fixing issues is also greatly appreciated. I am no graphics or animation expert - and I started this library in order to help others like me who just want to use a simple animation in their projects and be done with :-)
