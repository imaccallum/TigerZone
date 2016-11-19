## This currently doesn't work. See header of same size (near bottom) for details.

# Go into the directory with the server code
For me, this meant putting this into the command prompt:
cd IdeaProjects/TigerZone/src/serverExample
Find wherever your code is saving, go to that pathway

# Make sure your jdk has the correct path
For me, this meant putting this into the command prompt:
set path=%path%;C:\Java\jdk1.8.0_73\bin
You might have the same directory; you might have a different one. That's the specific path to get to the jdk bin file on my laptop.

# Compile the code using javac
Put this into the command prompt:
javac *.java
This will compile all the files in the directory (KnockKnockServer, KnockKnockClient, and KnockKnockProtocol)

# Go back to the src file
Put this into the command prompt:
cd ../

# Run the Server program
Put this into the command prompt:
java serverExample.KnockKnockServer 4444

# Open a seperate command prompt to act as the Client

# Run the Client program

## This is where it gets sticky for me. I'm fairly certain it's because knockknockserver.example.com isn't an actual host, but I don't know any hosts that I'd be able to use.
 
Put this into the command prompt:
java serverExample.KnockKnockClient knockknockserver.example.com 4444


In theory, this is the point where you'd see "Server: Knock! Knock!"
But until the last line can be figured out/a host found, it gives "Couldn't get I/O for the connection to knockknockserver.example.com" error.