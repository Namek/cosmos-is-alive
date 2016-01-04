java -jar runnable-texturepacker.jar 2_to_join\basic 3_out_textures\basic
java -jar runnable-texturepacker.jar 2_to_join\planets 3_out_textures\planets
xcopy /S /I /Y 3_out_textures\* ..\core\assets\graphics\
