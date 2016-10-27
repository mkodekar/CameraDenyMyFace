# CameraDenyMyFace
This is a sample application explaining how to deny selfie mode on apps that require some spefic stuffs like bills or ecg reports 
its basically written for my office work but the example explains how to use it.

The issue with this approach is somehow  it very nice for a objects that are near to the camera itself rather than to those who are far away, it considers artifacts or un identified objects as faces , don't be embarassed if it considers your face as artifact or an un identified object. 

Its not the example its the fucking android face detector class.

A better approach to face detection is present google play services lib which does not have the issue declared above.

there are various other libraries with glide and picasso which additional stuffs with face detection. 

dont use this if you hate bitmap or bitmap factory.


there is a demo [apk](https://github.com/mkodekar/CameraDenyMyFace/raw/master/apk/app-debug.apk) also available check it out, i have wrote this example with just the flow and have not added permission management. please give permission to the app by going into settings , rather than telling me it does not work.


thanks and credits to @mobapptuts for the base of the camera project with this

originally based upon
https://github.com/mobapptuts/camera_intent
