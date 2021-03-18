const fs = require('fs');
const path = require('path');

const serverPath = process.argv[2];
const docsFilesPath = path.join(serverPath, 'plugins', 'SkriptHubDocsTool', 'documentation');

if (!serverPath) {
    throw new Error("You need to specify the path to your server in argument!");
}

fs.readdir(docsFilesPath, (err, files) => {
    if (err) throw err;
    for (const docsFile of files) {
        const parsedFile = JSON.parse(fs.readFileSync(path.join(docsFilesPath, docsFile)));
        if (parsedFile.conditions) {
            const asyncEffects = parsedFile.conditions.filter(condition => condition.id.startsWith('Eff'));
            const conditions = parsedFile.conditions.filter(condition => !condition.id.startsWith('Eff'));
            if (parsedFile.effects) {
                parsedFile.effects.push(asyncEffects);
            } else {
                parsedFile.effects = asyncEffects;
            }
            parsedFile.conditions = conditions;
            fs.writeFileSync(path.join(docsFilesPath, docsFile), JSON.stringify(parsedFile, null, 2));
        }
    }
})