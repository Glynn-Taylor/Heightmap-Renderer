##About
NOTE: This source is not the finalised source that was submitted, that seems to have been a victim of time/reformatting.

FFor my extended project I decided to learn how to use OpenGL, by looking at how to create voxel terrain. As to not make the project too complex, seeing as I had no prior OpenGL experience, I decided to create a program that could simply read an image, convert it to greyscale and then generate a voxel heightmap based on that (shown above). During this project I also developed a front end menu system built upon a state based game loop.

This project taught me a lot, especially about OpenGL and how to approach projects; even though OpenGL seemed so annoying at first, it is now one of my favourite APIs to use in projects.

##Features
- Renders a heightmap from a PNG image using OpenGL
- Simple to use GUI
- Relative heights are shown by different textures (water, sand and grass)
- Still to do
- Optimisations for loading larger images
- Better camera control
- Interleaved VBOs

##Technical information
This project was written in Java, using LWJGL for access to OpenGL; Slick util libraries were also used for unicode fonts and image handling. Voxels are optimised by only drawing faces next to air blocks; on load every face is stored within several VBOs, as to reduce the render calls per frame.

##Links

- [WarwickGameDesign](https://www.warwickgamedesign.co.uk)
  - [Forums](https://www.warwickgamedesign.co.uk/forum)
  - [Games](https://www.warwickgamedesign.co.uk/games)

##Builds
- [Windows Linux Mac](http://gtaylor.warwickcompsoc.co.uk/downloads/heightmap-renderer/HeightMapRenderer_win-mac-linux_14-09-2012.zip)
