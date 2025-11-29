package com.astrobookings.adapters.out.out;

import com.astrobookings.core.application.ports.NotificationService;
import com.astrobookings.core.application.ports.PaymentGateway;

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
