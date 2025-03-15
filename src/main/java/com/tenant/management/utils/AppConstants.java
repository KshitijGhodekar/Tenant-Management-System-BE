package com.tenant.management.utils;

//Authors : K S SRI KUMAR
//Id : 24177474

public class AppConstants {

    public enum ApplicationStatus {
        PENDING,
        APPROVED,
        REJECTED,
        WITHDRAWN
    }

    public enum PropertyVisitStatus {
        PENDING,
        APPROVED,
        REJECTED,
        CANCELLED
    }

    public enum IssueStatus {
        OPEN,
        RESOLVED,
        CLOSED
    }

    public enum OccupationCategories {
        STUDENT,
        RETIRED,
        SKILLED_PROFESSIONAL,
        UNSKILLED_PROFESSIONAL,
        PART_TIME
    }
}
