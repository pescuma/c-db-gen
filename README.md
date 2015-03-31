A set of code generator and libs to map C++ object to databases.

Currently there are modules to generate code for:
  * Palm: access records when using palm SDK
  * Palm Conduit: convert from raw bytes to object
  * SQLite in C++: quick access to the DB as objects (no pointer management - quickly access the object, change, and put it back)
  * SQLite in VB6: quick access to the DB as objects (access the object, change, and put it back)

These are not intended to be feature complete. I keep implementing it on an as needed basis.