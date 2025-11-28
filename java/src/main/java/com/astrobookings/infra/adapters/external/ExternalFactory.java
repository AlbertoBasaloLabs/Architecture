package com.astrobookings.infra.adapters.external;

import com.astrobookings.business.ports.out.NotificationService;
import com.astrobookings.business.ports.out.PaymentGateway;

public class ExternalFactory {
    private static final PaymentGateway paymentGateway = new PaymentGatewayImpl();
    private static final NotificationService notificationService = new NotificationServiceImpl();

    public static PaymentGateway getPaymentGateway() {
        return paymentGateway;
    }

    public static NotificationService getNotificationService() {
        return notificationService;
    }
}
