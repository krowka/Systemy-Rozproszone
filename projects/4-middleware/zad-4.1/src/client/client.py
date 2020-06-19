import grpc
import exchange_rate_pb2
import exchange_rate_pb2_grpc


def currency_rate_to_string(currency_rate):
    return exchange_rate_pb2.CurrencyCode.Name(currency_rate.currencyCode) + " " + str(currency_rate.value)


def print_notification(notification):
    currency_rate_list = notification.currencyRate
    print("====================================")
    if len(currency_rate_list) == 2:
        print("PREVIOUS RATE: " + " " + currency_rate_to_string(currency_rate_list[1]))
    print("CURRENT RATE: " + " " + currency_rate_to_string(currency_rate_list[0]))
    print("MESSAGE: " + notification.message)
    print("====================================\n")


def make_currency_rate(currency_code, value):
    currency_enum = exchange_rate_pb2.CurrencyCode.Value(currency_code)
    return exchange_rate_pb2.CurrencyRate(currencyCode=currency_enum, value=float(value))


def make_condition(relation, currency_code, value):
    relation_enum = exchange_rate_pb2.RelationOp.Value(relation)
    currency_rate = make_currency_rate(currency_code, value)
    return exchange_rate_pb2.Condition(relationOp=relation_enum, currencyRate=currency_rate)


def subscribe(stub, currency_code, value, relation):
    print("[INFO] subscribe, condition: {0}, currency : {1}, value: {2}\n\n".format(relation, currency_code, value))
    condition = make_condition(relation, currency_code, value)
    notifications = stub.Subscribe(condition)
    for notification in notifications:
        print_notification(notification)


def run():
    with grpc.insecure_channel('localhost:50051') as channel:
        stub = exchange_rate_pb2_grpc.ExchangeRateStub(channel)
        print("Supported currencies:", exchange_rate_pb2.CurrencyCode.keys())
        currency_code = input("Enter currency \n==> ")
        value = float(input("Enter desired value \n==> "))
        if value < 0.0:
            raise Exception("Currency value must be a positive number. Got value: " + str(value))
        relation = input("Enter condition: LT (less than) or GT (greater than) \n==> ")
        subscribe(stub, currency_code, value, relation)


if __name__ == '__main__':
    run()
