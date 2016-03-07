# sea-of-decay
Sea of Decay - a roguelike game made in Java inspired by Studio Ghibli's Valley of the Wind.

I made some modifications to AsciiPanel and XPReader. First of all, I made AsciiPanel use a square font file,
and I made it use a more standard font file layout (IBM cp437). As for XPReader, I changed REXReader to use
a resource stream instead of File. This way I can put all the resource files in a folder in the project root.
Because of this I use the apache IOUtils, because REXReader converts the file to a byte array. 
