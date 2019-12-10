Important information:

This program does not come with any necessary JSON parser. The program utilizes its own parser in order to process the CSV lines. 

There is also a definite time issue when the CSV file is processed, although I have tried to work around that as best I can. But I guess it does give the user time to read the information I provide at the start about the program and how it works before it provides the interface for name input. 

Also, when I was creating this program, I initially wasn't sure how the implementation of other files on the command line worked.  As such, I had placed the CSV file in the same folder and had a BufferedReader pull from it directly. But now that I know how it works (that being that the (String[] args) refers to anything inputted after the file's name when initiated on the command line), I changed a few things around to make it fit with that, but I still haven't moved the database file out of the folder as the other files. As such, it's important for me to say that when you launch the implementation, you only need to input it as:

java CS245A2 tmdb_5000_credits.csv

The aforementioned "credits" file can also be found in this Google Doc: https://drive.google.com/file/d/1byQ1PmbydVO49niqrlYEbvbpGtxgBzPX/view
