package com.example.menudemo.ui.utills;

public class Task { ;


        private String messionid;
        private String messionname;
        private String messiontype;
        private String initiator;
        private String acceptor;
        private String price;
        private String address;
        private String deadline;
        private String launchtime;
        private String status;
        private String details;

        public String getMessionid() {
            return messionid;
        }

        public void setMessionid(String messionid) {
            this.messionid = messionid;
        }

        public String getMessionname() {
            return messionname;
        }

        public void setMessionname(String messionname) {
            this.messionname = messionname;
        }

        public String getMessiontype() {
            return messiontype;
        }

        public void setMessiontype(String messiontype) {
            this.messiontype = messiontype;
        }

        public String getInitiator() {
            return initiator;
        }

        public void setInitiator(String initiator) {
            this.initiator = initiator;
        }

        public String getAcceptor() {
            return acceptor;
        }

        public void setAcceptor(String acceptor) {
            this.acceptor = acceptor;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public String getLaunchtime() {
            return launchtime;
        }

        public void setLaunchtime(String launchtime) {
            this.launchtime = launchtime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public Task( String messionid,String messionname, String messiontype,
                     String initiator, String price, String address, String deadline,
                     String launchtime, String status, String details) {
            super();
            this.messionid=messionid;
            this.messionname = messionname;
            this.messiontype = messiontype;
            this.initiator = initiator;
            this.price = price;
            this.address = address;
            this.deadline = deadline;
            this.launchtime = launchtime;
            this.status = status;
            this.details = details;
        }

        public Task(String messionid,String messionname, String messiontype,
                    String initiator, String acceptor, String price, String address,
                    String deadline, String launchtime, String status, String details) {
            super();
            this.messionid=messionid;
            this.messionname = messionname;
            this.messiontype = messiontype;
            this.initiator = initiator;
            this.acceptor = acceptor;
            this.price = price;
            this.address = address;
            this.deadline = deadline;
            this.launchtime = launchtime;
            this.status = status;
            this.details = details;
        }

    @Override
    public String toString() {
        return "Task{" +
                "messionid='" + messionid + '\'' +
                ", messionname='" + messionname + '\'' +
                ", messiontype='" + messiontype + '\'' +
                ", initiator='" + initiator + '\'' +
                ", acceptor='" + acceptor + '\'' +
                ", price='" + price + '\'' +
                ", address='" + address + '\'' +
                ", deadline='" + deadline + '\'' +
                ", launchtime='" + launchtime + '\'' +
                ", status='" + status + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
