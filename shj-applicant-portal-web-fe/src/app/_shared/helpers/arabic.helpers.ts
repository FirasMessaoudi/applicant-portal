export function isInputInArabic(input : string) {
    let isArabic = /^([\u0600-\u06ff]|[\u0750-\u077f]|[\ufb50-\ufbc1]|[\ufbd3-\ufd3f]|[\ufd50-\ufd8f]|[\ufd92-\ufdc7]|[\ufe70-\ufefc]|[\ufdf0-\ufdfd]|[\u00C0-\u00D6\u00D8-\u00f6\u00f8-\u00ff-\ ])*$/g;

    return isArabic.test(input);
  }

  export function stringContainsOnlyArabicChars(input : string) {
    let isArabic = /^([\u0600-\u06ff]|[\u0750-\u077f]|[\ufb50-\ufbc1]|[\ufbd3-\ufd3f]|[\ufd50-\ufd8f]|[\ufd92-\ufdc7]|[\ufe70-\ufefc]|[\ufdf0-\ufdfd])*$/g;

    return isArabic.test(input);
  }