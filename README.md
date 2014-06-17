## Start a (Bukkit) Minecraft server:
* Download a bukket jar: https://dl.bukkit.org/downloads/craftbukkit/
* follow instructions to create startup-script (mac os): http://wiki.bukkit.org/Setting_up_a_server
* Don't forget to create the start script (start_server.command)
* start and stop (this creates some required files and directories)
  * `./start_server.command` which starts a console
  * Stop with `stop` in the console that opens up

## Install the Clojure REPL Bukkit Plugin:
* clone/download CmdrDats/clj-minecraft project
  * mine is here (providing a scratch ns with experiments and examples): https://github.com/mudphone/clj-minecraft
  * The original works just as well: https://github.com/CmdrDats/clj-minecraft
* `lein uberjar` the project
* copy standalone jar from targets dir to bukkit plugins directory
  * plugins install instructions here: http://wiki.bukkit.org/Installing_Plugins
* start minecraft (using start script)

## Connect your player to the (Bukkit) Minecraft server:
* get a minecraft gui/launcher and account (pay for this)
* start gui/launcher
* make sure your gui/launcher is using the same version as the bukkit jar you downloaded
  * Look at the settings to confirm the version number. Bukkit requires a matching version number.
* use gui/launcher to connect to server (0.0.0.0 if local), multiplayer
* now you're playing minecraft, it should work like any other server

## Connect your REPL to the Bukkit plugin:
* start a clojure REPL (either in Emacs, or via lein repl)
  * M-x cider
  * (enter host, which should be 127.0.0.1 for local default)
  * (enter port, which should be 4005 by default)

* do cool stuff

### Bukkit Commands/Hot-Keys:
Run these in the server console that opens up with the startup script (start_server.command).
* http://wiki.bukkit.org/CraftBukkit_commands

Change to invincible / creative mode:
* `gamemode <0/1/2> <username>`
* `gamemode 1 MudphoneCraft`

Change time of day to dawn: 
* `time set 0`

Access Inventory:
`e`

Access Console:
`t`

### HALP
* See the [original project README](https://github.com/mudphone/clj-minecraft/blob/master/README.md) for help.
* Also, see [Bukkit API docs](http://jd.bukkit.org/rb/doxygen/) for what's possible.
* Bukkit docs on materials: http://jd.bukkit.org/rb/apidocs/org/bukkit/Material.html
* Don't forget, in the REPL you can eval an arbitrary Clojure ns, so you have the full power of Clojure at your fingertips!

## LICENSE

The MIT License (MIT)

Copyright (c) 2014 Kyle Oba

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.