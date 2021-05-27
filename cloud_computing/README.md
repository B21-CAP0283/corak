# This page contain what Cloud Computing's team do

## 1. Make database on Firestore and setting up the API

#

## 2. Make cloud storage for image and mp3

#

## 3. Make mp3 file using Google text2speech API
The modern Text-to-Speech software allows you to create a voice transcribe your readings 
with the natural/ human-professional sound like in the TV. 

**1.Prepare Your Google Cloud account and enable Text-to-Speech service**

Enable the Cloud Text-to-Speech API which you can find this service in APIs & Services menu

Create a service account key which you can find under APIs & Services > Credentials > Create Credentials > Service account

continue by 1. selecting a New service account, 2. give a name to your service account, 3. click CREATE to confirm. (You do not need to select Role and please left the Key type as JSON), and 4. confirm to Create without Role as in the image below. After that, the Private key file will be saved to your computer. Please, keep it secure as it will be used as a key that linked with your Google Cloud project.

**2.Prepare Node.js in Your PC**

This step is pretty simple. Just download and install Node.js. After it is done, please confirm that your Node.js is installed successfully.

In Window System, just 1. type “cmd” at the search panel, 2. open the Command Prompt, 3. Type “node -v” then you get an answer back as 'v.xx.xx.xx.'

**3.Write the Node.js program**

After everything is prepared, write the software to consume the Google Cloud Text-to-Speech API. First, prepare the code editor which you can should any text editor you like such as Atom, VSCode, or, Notepad++.

After you prepared it, create a project folder and place your Private key file from step 1 in this folder. Then, open the Command Prompt/ console/ terminal at the project directory. make sure that you are in the correct directory by using ls command and you change directory using cd command. 

Then, use the following commands to initiate your Node.js program and then install the necessary dependency package from Google Cloud.

```bash
$npm init 
$npm install @google-cloud/text-to-speech
```

Then, create a new file called text2speech.js In this file, copy the following codes and paste it in your file. Please, note that you should assign your projectID and keyFilename variable with your project ID on Google Cloud and your private key name respectively.

```bash
const textToSpeech = require('@google-cloud/text-to-speech');
const fs = require('fs');
const util = require('util');
const projectId = 'PROJECTID'
const keyFilename = 'KEYFILENAME.json'
const client = new textToSpeech.TextToSpeechClient({ projectId, keyFilename });
const YourSetting = fs.readFileSync('setting.json');
async function Text2Speech(YourSetting) {
  const [response] = await client.synthesizeSpeech(JSON.parse(YourSetting));
  const writeFile = util.promisify(fs.writeFile);
  await writeFile(JSON.parse(YourSetting).outputFileName, response.audioContent, 'binary');
  console.log('Audio content written to file: ${JSON.parse(YourSetting).outputFileName}');
}
Text2Speech(YourSetting);
```

Next, create a setting file called setting.json You can construct this file with the following codes. 

At “input text” you can change your text input to whatever text you want to be transcribed. at “voice” you can set the input language and input sound you like. You can also change the output file name at “outputfilename” in this setting file.

```bash
{
    "audioConfig": {
      "audioEncoding": "LINEAR16",
      "pitch": 0,
      "speakingRate": 1.00
    },
    "input": {
      "text": " contoh "
    },
    "voice": {
      "languageCode": "id-ID",
      "name": "id-ID-Wavenet-A"
    },
    "outputFileName":"contoh.mp3"
  }
  ```
  
  Now everything is set. You can just test-run the program by using the following command in your terminal/ command prompt:
  
```bash
  $ node text2speech.js
>> Audio content written to file: output.mp3
```

Your output will be in the project folder with the name you assign in the setting file.







