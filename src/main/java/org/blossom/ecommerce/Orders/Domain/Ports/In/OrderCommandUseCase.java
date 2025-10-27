package org.blossom.ecommerce.Orders.Domain.Ports.In;

import org.blossom.ecommerce.Orders.Domain.Models.Order;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.spec.*;

import java.util.Optional;

public interface OrderCommandUseCase {

    Optional<Order> create(CreateOrderSpec spec);
    Order update(UpdateOrder cmd);
    void delete(OrderId id);

    Order pay(PayOrder cmd);
    Order cancel(CancelOrder cmd);
    Order markAsDelivered(OrderId id);

    Order markAsDelivered(MarkAsDelivered cmd);

    Order refund(RefundOrder cmd);

    Order addItem(AddOrderItem cmd);
    Order removeItem(RemoveOrderItem cmd);
    Order applyDiscount(ApplyDiscount cmd);
}