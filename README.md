# MCEmu

Run emulators in Minecraft!

![2022-05-02_19 53 53](https://user-images.githubusercontent.com/493908/166223486-5f2ee0b9-7c17-422d-953b-747ba1c6beb4.png)

## Status

just an incubation not a production! 

## Requirements

 * minecraft 1.18.2
 * fabricmc
 * romz
```shell
$ mkdir $MINECRAFT_HOME/config/mcemu/roms/nes
$ cp your_roms.nes ... $MINECRAFT_HOME/config/mcemu/roms/nes/
```

## TODO

 * ~~television entity renderer~~
 * ~~console block renderer~~
 * save nes resource when not viewing
 * multiple cartridges are not tested
 * use a new cartridge and reset does not work now
 * the nes emulator seems to run w/o wait