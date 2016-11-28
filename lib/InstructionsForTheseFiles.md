For those without experience doing this (like me), here's how I did it:

# Download the four .jar files in this GitHub folder onto your computer
- byte-buddy-1.5.5.jar
- byte-buddy-agent-1.5.5.jar
- mockito-core-2.2.22.jar
- objenesis-2.4.jar

# Find the classpath within IntelliJ
- Files => Project Structure => SDKs
- The tab at the top should default to classpath

# Find the location where most of these files in the classpath are coming from
- For me, they were all .jar files in C:\Program Files(x86)\Java\jdk1.8.0_65\jre\lib

# Place the four newly downloaded .jar files into that folder
- (This probably isn't necessary, but won't hurt to organize them all into one place)

# Back within Files => Project Structure => SDKs
- Press the + symbol at the right side of the window
- Find the folder you just moved the .jar files into
- Select the four new files (ctrl+click each of them to select all four at once)
- Press OK
- Press Apply

# Restart IntelliJ

# You should be good to go :)