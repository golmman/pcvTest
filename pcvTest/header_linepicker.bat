@echo Creating class file
"C:\Program Files\Java\jdk1.8.0_45\bin\javac.exe" -d bin src/pcv/algo/LinePickerNative.java
@echo done!
@echo(

@echo Creating C header file
"C:\Program Files\Java\jdk1.8.0_45\bin\javah.exe" -o linepicker_native.h -classpath bin pcv.algo.LinePickerNative
@echo done!
@echo(

pause