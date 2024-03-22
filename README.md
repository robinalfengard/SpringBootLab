Applikationens Struktur: Applikationen ska vara databasdriven och hantera användare samt deras
publicerade meddelanden. Gränssnittet ska huvudsakligen bestå av HTML-sidor, men ett REST
API ska också skapas under endpointen /api/ för att tillhandahålla öppen åtkomst till viss
information som inte kräver inloggning.
Inloggning: För att logga in på servern ska OAuth2-login användas, med minst stöd för inloggning
via GitHub men andra tjänster kan också läggas till. Vi behöver lagra användare lokalt i en User
tabell och vilka roller de har men själva authentication delen sköter Github.
Spring Security 5 - OAuth2 Login | Baeldung

För godkänt ska applikationen ha:
Funktionalitet för Meddelanden
Via html websida ska vi erbjuda följande funktionalitet:
• Ej inloggade användare ska kunna visa publicerade och publika meddelanden.
• Inloggade användare ska kunna visa alla publicerade meddelanden, inklusive de som inte är
publika.
• Meddelanden kan visas baserat på vem som har publicerat dem.
• Meddelanden hämtas i omgångar, antingen via en länk eller automatiskt när sidan scrollas
nedåt.
• Användaren som skrev ett meddelande ska kunna redigera det.
• Meddelanden ska lagra information om vem som senast ändrade dem och när.
https://docs.spring.io/spring-data/jpa/reference/auditing.html
• Inloggning och utloggning från applikationen.
• Internationalization: Val av språk på hemsidan, minst svenska och engelska ska stödjas.
• Översättning av meddelanden med hjälp av externt api från spring boot.
• https://github.com/LibreTranslate/LibreTranslate
• Dokumentation av api endpoints med hjälp av swagger och openapi

Användarinformation:
• En inloggad användare ska kunna modifiera sin egen information, inklusive:
• Profilnamn som visas för andra (ska vara unikt).
• Ladda upp en profilbild (om ingen profilbild valts ska en standardbild användas).
• Fullständigt namn och e-postadress (dessa uppgifter visas inte för andra).
