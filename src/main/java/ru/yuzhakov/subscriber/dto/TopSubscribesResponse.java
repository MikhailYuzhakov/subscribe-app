package ru.yuzhakov.subscriber.dto;


public class TopSubscribesResponse {
    private Long id;
    private String serviceName;
    private Integer userCount;

    public TopSubscribesResponse(Long id, String serviceName, Integer userCount) {
        this.id = id;
        this.serviceName = serviceName;
        this.userCount = userCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
}
