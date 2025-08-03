package com.bluhawk.smtp;

public class Cryptography {
    private int publicKey;

    public Cryptography(int publicKey) {
        this.publicKey = publicKey;
    }

    public boolean verifySignature(Email mail) {
        String expected = decrypt(mail.getSignature());
        String actual = mail.getSubject() + mail.getBody();
        return expected.equals(actual);
    }

    private String decrypt(String signed) {
        StringBuilder sb = new StringBuilder();
        for (char ch : signed.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                sb.append((char)((ch - base - publicKey + 26) % 26 + base));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
