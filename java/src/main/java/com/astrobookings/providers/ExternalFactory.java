package com.astrobookings.providers;

import com.astrobookings.business.ports.NotificationService;
import com.astrobookings.business.ports.PaymentGateway;

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
