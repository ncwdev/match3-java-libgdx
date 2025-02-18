# match3-java-libgdx

A demo of classic 3-in-a-row puzzle game implementation using LibGDX and Entity-Component-System (ECS) architecture.

## Features

- 🧩 **ECS Architecture**: Clean separation of game logic using Entity-Component-System pattern
- ⚡ **Event-Driven Systems**: Efficient communication between game systems
- 🎮 **Smooth Gameplay**:
  - Click-and-swap tile mechanics
  - Match detection (3+ in a row/column)
  - Cascading tile effects
  - Valid initial board generation
  - Animated tile movements
  - Texture-based rendering
  - Input locking during animations
  - Auto-refill of empty tiles

How to play:
1 Clone the repository
2 Type gradlew.bat lwjgl3:run
3 Enjoy

-------------------------------------------------------------------------------

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an empty `ApplicationListener` implementation.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
-------------------------------------------------------------------------------

Credits

Built with LibGDX, DeepSeek R1 and Windsurf.
