******************************* INSTALLING THE REQUIRED RESOURCES TO RUN THE TOOL ******************************

The tool that has been built is an amalgamation of a number of tools and resources. To extract data
traces and generate invariants, the BCT tool was used which internally uses the Daikon tool to
generate the invariants. All of these tools are command line based and they are currently being
supported to run on Windows based machines. 

****************************************************************************************************************

It is important to note that the Java SDK, Java JDK and AspectJ libraries are assumed to have already been installed.

Once these are installed, the following steps can be taken to install the BCT tool and its resources: 

Step 1: Extract the provided “Automated_Monitor_Generation.zip” file.
Step 2: Navigate to “Prerequisites” folder.
Step 3: Copy “bct.jar” inside your project file.
Step 4: Copy the “BCT” folder in a location of your choice (keep path to the folder)
Step 5: Set “path” environment variable to point to the “CVS” and “x86” folders located inside the “BCT”
folder.

************************88********************* TOOL MANUAL ****************************************************

To be able to automatically generate runtime monitors from tests, the following steps need to be carried out:

------------------------------------- PHASE 1 – INVARIANT GENERATION -------------------------------------------

To be able to generate invariants, we first have to instrument the system to be able to extract data
traces. Once instrumented, a number of tests need to be identified. These will be used to exercise the
system to enable the data trace extraction. The data traces extracted are then processed by the Daikon
tool that the BCT toolset uses to generate the Invariants.

Step 1: Compile the Java project to monitor
Step 2: Setup BCT tool to record data traces using the following command:

java console.SetupForJavaDataRecording <path/to/BctAnalysis> <path to folder with the classes to instrument or jars files>

Step 3: Execute the test cases using the following command:

java -Dbct.home=path/to/BctAnalysis/BCT/BCT_DATA/BCT org.junit.runner.JUnitCore 

Step 4: Process the traces and generate Invariants using the following command:

java console.InferJavaModels <path/to/BctAnalysis>

------------------------------------- PHASE 2- MONITOR GENERATION ---------------------------------------------

Once the Invariants have been generated, they can be passed on to our tool which processes the given
invariants and automatically translates them into runtime monitors.

Step 1: Generate runtime monitors using the following command:

java MonGen –b <Location/To/BctAnalysis> -m <Path/To/Monitor/Injection> -s <Path/To/Project/Source/Code>

------------------------ PHASE 3- RUNNING THE SYSTEM WITH THE GENERATED MONITORS ------------------------------

Given the monitors, they are then instrumented inside the live system using the AspectJ technology.

Step 1: Compile System to a jar file
Step 2: Weave monitor with system using the following command:

ajc -1.6 -cp aspectjrt.jar; -inpath <Path/To/System/Jar> -outjar <NameOfOutputJar>.jar <Path/To/Location/Of/Monitor>*.aj

Step 3: Run the weaved system using the following command:

aj5 -cp . -jar <NameOfOutputJar>.jar

****************************************************************************************************************************








