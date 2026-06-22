const WINDOWS_1252_REVERSE = {
  "€": 0x80,
  "‚": 0x82,
  "ƒ": 0x83,
  "„": 0x84,
  "…": 0x85,
  "†": 0x86,
  "‡": 0x87,
  "ˆ": 0x88,
  "‰": 0x89,
  "Š": 0x8a,
  "‹": 0x8b,
  "Œ": 0x8c,
  "Ž": 0x8e,
  "‘": 0x91,
  "’": 0x92,
  "“": 0x93,
  "”": 0x94,
  "•": 0x95,
  "–": 0x96,
  "—": 0x97,
  "˜": 0x98,
  "™": 0x99,
  "š": 0x9a,
  "›": 0x9b,
  "œ": 0x9c,
  "ž": 0x9e,
  "Ÿ": 0x9f,
};

const utf8Decoder = new TextDecoder("utf-8", { fatal: true });

function countMatches(text, regex) {
  return text.match(regex)?.length ?? 0;
}

function scoreText(text) {
  const hangulCount = countMatches(text, /[가-힣]/g);
  const suspiciousCount = countMatches(text, /[ÃÂâìëêð³²¸¦¬€™œžŸ]/g);

  return hangulCount * 4 - suspiciousCount * 2;
}

function toByteArray(text) {
  const bytes = [];

  for (const char of text) {
    const code = char.charCodeAt(0);

    if (code <= 0xff) {
      bytes.push(code);
      continue;
    }

    const mapped = WINDOWS_1252_REVERSE[char];

    if (mapped == null) {
      return null;
    }

    bytes.push(mapped);
  }

  return Uint8Array.from(bytes);
}

function decodeMojibakeOnce(text) {
  const bytes = toByteArray(text);

  if (!bytes) {
    return text;
  }

  try {
    return utf8Decoder.decode(bytes);
  } catch {
    return text;
  }
}

export function repairMojibake(text) {
  if (typeof text !== "string" || text.length === 0) {
    return text;
  }

  let current = text;

  for (let i = 0; i < 3; i += 1) {
    const decoded = decodeMojibakeOnce(current);

    if (decoded === current) {
      break;
    }

    const currentHasHangul = /[가-힣]/.test(current);
    const decodedHasHangul = /[가-힣]/.test(decoded);
    const currentScore = scoreText(current);
    const decodedScore = scoreText(decoded);

    if (decodedHasHangul || decodedScore > currentScore) {
      current = decoded;
      continue;
    }

    if (currentHasHangul && !decodedHasHangul) {
      break;
    }

    break;
  }

  return current.normalize("NFC");
}

export function normalizeApiText(value) {
  if (typeof value === "string") {
    return repairMojibake(value);
  }

  if (Array.isArray(value)) {
    return value.map(normalizeApiText);
  }

  if (value && Object.prototype.toString.call(value) === "[object Object]") {
    return Object.fromEntries(
      Object.entries(value).map(([key, entryValue]) => [key, normalizeApiText(entryValue)]),
    );
  }

  return value;
}
