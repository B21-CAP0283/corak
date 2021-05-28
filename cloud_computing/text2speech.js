const textToSpeech = require('@google-cloud/text-to-speech');
const fs = require('fs');
const util = require('util');
const projectId = 'corak-314111'
const keyFilename = 'corak-314111-cc095a6bf52e.json'
const client = new textToSpeech.TextToSpeechClient({ projectId, keyFilename });
const YourSetting = fs.readFileSync('setting.json');
async function Text2Speech(YourSetting) {
  const [response] = await client.synthesizeSpeech(JSON.parse(YourSetting));
  const writeFile = util.promisify(fs.writeFile);
  await writeFile(JSON.parse(YourSetting).outputFileName, response.audioContent, 'binary');
  console.log('Audio content written to file: ${JSON.parse(YourSetting).outputFileName}');
}
Text2Speech(YourSetting);