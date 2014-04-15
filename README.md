offworld
========

Post-Apocalyptic Pizza Delivery Game using OpenGL and Box2D

I worked on this game for a little over a year. It originally started as an android-only 2D platformer,
but I ported it over to the desktop and theoretically it can run on iOS as well.

The game uses a custom rendering engine I programmed from scratch that uses OpenGl 2.x and is compatible with OpenGl ES 2.x; everything is done in shaders. The engine uses LWJGL for OpenGL calls and gahtering input and currently works on Windows, Linux, OS X, and Android. Theoretically it works on iOS as well but I never got a chance to test it.

All of the engine code is in the `guts` (Game UTils) folder. The game itself, Offworld, was created using solely code from guts, which includes systems for making GUIs, gathering input, performing AI and pathfinding, and some rudimentary networking that I never got to flesh out.

All the base code for offworld is in the `offworld` folder. Platform-specific code, of which there is very little, is located in the respective folders. The project can easily be compiled and run in Eclipse.

The rendering engine can handle 2D and 3D rendering, but all physics is 2D. It wouldn't be too much work to switch to a 3D physics engine, though.

Assets for the game are in the `offworld-android` folder. In the `raw` folder is various SVGs and Blender files that were used to generate the assets for the game. All textures, GUI and characters were done as SVGs in Inkscape. Animations were done with Anime Studio and exported as sprite sheets packed with the wonderful TexturePacker. I tried to use as much free and open source to make this as I could, but sometimes it can't be helped.

Levels are stored as three seperate .obj files- one for rendering that includes texture coordinates to allow for easily wrapping textures, one for geometry that is used for collision data, and one for debug rendering. The .obj files were all created in Blender.

Resources are loaded from JSON files. Looking at `base.res` you can see all of the resource files loaded when the game starts. Ideally, resources would be loaded on a level-by-level basis but I never got around to that.

Lots of work went into the controls for the game. On a touch screen, there are three buttons on each side of the screen for moving left/right and jumpting. Touching anywhere will shoot. Controls are similarly simple with a controller- only two buttons, jump and shoot. Movement is done with the left thumbstick and aiming with the right. It's a blast to fly around with the jetpack and shoot stuff!

I've pretty much abandoned this project for now, but there is a ton of great code in here. I put a lot of work into the pathfinding for the AI but it's a little incomplete. There are lots of fun OpenGL shaders in `offworld-android/assets/shaders/sandbox` that were created by messing around with a shader toy. I had a lot of story written, I might add it all to the repo at some point in the future.

Feel free to use anything however you see fit, license is WTFPL. I'd appreciate a shout-out or a message letting me know that you're using my work- I'd love to see what you do with it!

Enjoy!
TranquilMarmot (Nate Moore)
<nate272@gmail.com>
