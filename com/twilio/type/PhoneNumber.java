//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.twilio.type;

import com.google.gson.annotations.Expose;

import java.net.URLEncoder;
import java.util.Objects;

public class PhoneNumber implements Endpoint {

    @Expose // The purpose of this class is to be able to store PhoneNumber objects in the backup file.
    private final String rawNumber;

    public PhoneNumber(String number) {
        this.rawNumber = number;
    }

    public String getEndpoint() {
        return this.rawNumber;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            PhoneNumber other = (PhoneNumber)o;
            return Objects.equals(this.rawNumber, other.rawNumber);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.rawNumber});
    }

    public String toString() {
        return this.rawNumber;
    }

    public String encode(String enc) {
        try {
            return URLEncoder.encode(this.rawNumber, enc);
        } catch (Exception var3) {
            return this.rawNumber;
        }
    }
}
