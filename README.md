# MyFeed

Andrey is a Tech Lead who will lead a project called MyFeed. It is a simple Android project which have some requirements to insert urls and load them to be a video or image in the screen. The detail is on this figma https://www.figma.com/file/NFQqrhDqQF2rUZ1E21Emk1/Technical-Skill-Test-Design?node-id=14%3A154. 

This project is written in `Kotlin` and will use the architecture that google recommend us as an android engineer. So Andrey and his team will be following that architecture recommendation that consist of
- Jetpack and its libraries
  - LiveData
  - ViewModel
  - DataBinding
  - Navigation
  - Coroutine

With that in mind, we are hoping that this project will be really maintainable for long term, because the architecture we use is the one that google said will support. So, the team will find those architecture being used in the project.

Besides the architecture, as mentioned in this project we are required to show image and video. To do that, Andrey also chooses the best framework that will help development and also considering the support and the community that framework has. And, that's why Andrey chooses 
- `ExoPlayer` to show video and 
- `Glide` to show Image.
All those frameworks are combined to be used seamlessly to achieve the requirement.

In terms of the android project itself, even this project is simple, Andrey also created a mechanism to having different flavors for let's say we want to have different environment in the app, one for testing and one for production. These flavors will really help us to differentiate the environment without need to hardcode and maybe comment or uncomment code, just to serve that purpose. It is written in the build.gradle configuration.

In the future, when team is getting bigger and the requirements also become complex, we can have the way to separates features by module. So this project will have *multi module* with different dependencies configured by its `build.gradle` and have their own package. It will really help to speed up the development and separating concern.

It is not created yet, but the architecture we have (MVVM) will allow us to create the Unit Test for our view model, so we can ensure the logic is well tested and covered by test. The dependencies needed is already setup.

So, 
1. Just clone this repo
2. Open it in an Android Studio
3. Wait until the setup of the project is finished
4. Run the app to your simulator or your Android device

If you have any questions please let Andrey know andrey.yoshua@gmail.com

