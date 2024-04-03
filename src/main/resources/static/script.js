async function translateText(text, sourceLanguage, targetLanguage) {
    const response = await fetch("https://libretranslate.com/translate", {
        method: "POST",
        body: JSON.stringify({
            q: text,
            source: sourceLanguage,
            target: targetLanguage
        }),
        headers: { "Content-Type": "application/json" }
    });

    if (response.ok) {
        const data = await response.json();
        console.log(data.translatedText); // Output the translated text
    } else {
        console.error("Error translating text.");
    }
}

