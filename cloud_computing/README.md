# This page contain what Cloud Computing's team do

## 1. Make database on Firestore and setting up the API
Go to google console and make a project, choose billing account, and basic stuff. After choosing the project has been created before, we do:

**1. Go to Firestore from navigation menu**

On Data panel, we choose native mode, location and then create.

After that, we start make a collection. For the collection ID we choose our project name, which is corak. 
For the each of document ID we use the data name from our dataset (batik-bali, batik-cendrawasih, etc).

We make for each document 6 field and fill the value based on our research from dataset:
- name
- characteristic
- history
- origin
- philosophy
- audio (for the text2speech mp3 link)

For setting up the API, we go from console.firebase.google.com

We create project, the firebase console automatically detect the project on our GCP

After setting up, check check the terms, then we choose Android for App we want to build.

Choose the name of the project, and at the end, we got file _google.services.json_.
We gave this file to Android team for API Config, auth, etc.

After that, we go to firestore database panel on sidebar, and choose rules column.
Because we dont have authentication, we set the rules to allow to read, and prohibited to write.
The code should look like this

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow write: if false;
      allow read: if true;
    }
  }
}
```

Then publish it


## 2. Make cloud storage for image and mp3

Go to google console and choose the project, then go to Cloud storage from navigation menu

Choose the one **without** 'staging' name

Make 2 folder:
- images (for saving the uploaded taken image from user )
- mp3 (for saving the mp3 file from text2speech feature)

for each of the mp3 file, we name it in accordance with the label of dataset name (batik-bali.mp3, batik-cendrawasih.mp3, etc)

**For Permissions Images**
Go to console.firebase.google.com, choose the project and go to Build > Storage in sidebar

After that go to Rules column

Here, we changed the permissions to access data (both read and write without Authentication), so the code looks like this:
```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      // allow read, write: if request.auth != null;
      allow read, write: if true;
    }
  }
}

```
Then publish it. Now users can upload image to the storage

**For Permissions mp3**

After we got mp3 file for each data from the text2speech, we upload it to the folder

Then we can click on the 3 dots on the far for each file, click edit permissions

Click Add Entry and change the entity to **Public**, left the name for **allUsers** and access for **Reader**

Save then. (We do it manually one by one for each file)

Now you can get the public URL by clicking on the file name and look for the public URL link

Copy to put it into the value on audio key in firestore field, so when the data send back to the request, it contains link to play the audio mp3 from text2speech


## 3. Make mp3 file using Google text2speech API
The modern Text-to-Speech software allows you to create a voice transcribe your readings 
with the natural/ human-professional sound like in the TV. 

**1.Prepare Your Google Cloud account and enable Text-to-Speech service**

Enable the Cloud Text-to-Speech API which you can find this service in APIs & Services menu

Create a service account key which you can find under APIs & Services > Credentials > Create Credentials > Service account

continue by 1. selecting a New service account, 2. give a name to your service account, 3. click CREATE to confirm. (You do not need to select Role and please left the Key type as JSON), and 4. confirm to Create without Role as in the image below. After that, the Private key file will be saved to your computer. Please, keep it secure as it will be used as a key that linked with your Google Cloud project.

**2.Prepare Node.js in Your PC**

This step is pretty simple. Just download and install Node.js. After it is done, please confirm that your Node.js is installed successfully.

In Window System, just 1. type ???cmd??? at the search panel, 2. open the Command Prompt, 3. Type ???node -v??? then you get an answer back as 'v.xx.xx.xx.'

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

At ???input text??? you can change your text input to whatever text you want to be transcribed. at ???voice??? you can set the input language and input sound you like. You can also change the output file name at ???outputfilename??? in this setting file.

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







