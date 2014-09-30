import org.apache.commons.io.FileUtils

File test1 = new File( basedir, "test-destination-1.txt");
File test2 = new File( basedir, "test-destination-2.txt");
File expected1 = new File( basedir, "test-expected-1.txt");
File expected2 = new File( basedir, "test-expected-2.txt");

assert test1.isFile()
assert test2.isFile()

assert FileUtils.contentEquals(test1, expected1)
assert FileUtils.contentEquals(test2, expected2)
