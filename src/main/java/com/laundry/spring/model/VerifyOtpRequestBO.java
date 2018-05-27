package com.laundry.spring.model;

public class VerifyOtpRequestBO {
        private String mobile;
        private String otp;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            VerifyOtpRequestBO that = (VerifyOtpRequestBO) o;

            if (mobile != null ? !mobile.equals(that.mobile) : that.mobile != null) return false;
            return otp != null ? otp.equals(that.otp) : that.otp == null;
        }

        @Override
        public int hashCode() {
            int result = mobile != null ? mobile.hashCode() : 0;
            result = 31 * result + (otp != null ? otp.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "VerifyOtpRequestBO{" +
                    "mobile='" + mobile + '\'' +
                    ", otp='" + otp + '\'' +
                    '}';
        }
}
