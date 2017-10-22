# Cryptography | [![Build Status](https://travis-ci.org/GUSAR1T0/Cryptography.svg?branch=master)](https://travis-ci.org/GUSAR1T0/Cryptography)

“Cryptography” is data encryption and decryption application based on Data Encryption Standard (DES) and Merkle-Hellman knapsack cryptosystem. The application has written on Java SE 8.

---

## How to build local

```
~/> git clone https://github.com/GUSAR1T0/Cryptography.git
~/> cd Cryptography
~/Cryptography> ./gradlew build
:compileJava
:processResources
:classes
:jar
:assemble
:compileTestJava
:processTestResources
:testClasses
:test
:check
:copyDeps
:copylib NO-SOURCE
:build

BUILD SUCCESSFUL

Total time: 8.197 secs
~/Cryptography> _
```

When you execute the last command, you will see the message status of it. Remember only `BUILD SUCCESSFUL` message will be positive result of build.

## How to use

This application has the certain list of options which used for running via command line interface:
```
usage: Cryptography
 -c,--cipher <arg>   Encrypt or decrypt, if it is not stated, program will do both cipher modes simultaneously
 -k,--generate-key   Generate one key for encryption of all files, it is only for data which should be encrypted
 -p,--path <arg>     Paths to files, can be used several times or never
```

## Examples of usage

* Encryption without key generation:
`~/Cryptography/build/libs> java -jar Cryptography.jar -c=encrypt p=<path1> p=<path2> ... p=<pathN>`
* Encryption with key generation:
`~/Cryptography/build/libs> java -jar Cryptography.jar -c=encrypt -k p=<path1> p=<path2> ... p=<pathN>`
* Decryption:
`~/Cryptography/build/libs> java -jar Cryptography.jar -c=decrypt p=<path1> p=<path2> ... p=<pathN>`
* Both cipher modes:
`~/Cryptography/build/libs> java -jar Cryptography.jar p=<path1> p=<path2> ... p=<pathN>`

*Notice*: option “-p” can be unset. If you miss it, the application will choose files by default which were put into resources directory.

## License

This code is under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0).

See the `NOTICE` file for required notices and attributions.
