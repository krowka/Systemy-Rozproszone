# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
import grpc

import exchange_rate_pb2 as exchange__rate__pb2


class ExchangeRateStub(object):
    """Missing associated documentation comment in .proto file"""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.Subscribe = channel.unary_stream(
                '/ExchangeRate/Subscribe',
                request_serializer=exchange__rate__pb2.Condition.SerializeToString,
                response_deserializer=exchange__rate__pb2.CurrencyNotification.FromString,
                )


class ExchangeRateServicer(object):
    """Missing associated documentation comment in .proto file"""

    def Subscribe(self, request, context):
        """Missing associated documentation comment in .proto file"""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_ExchangeRateServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'Subscribe': grpc.unary_stream_rpc_method_handler(
                    servicer.Subscribe,
                    request_deserializer=exchange__rate__pb2.Condition.FromString,
                    response_serializer=exchange__rate__pb2.CurrencyNotification.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'ExchangeRate', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))


 # This class is part of an EXPERIMENTAL API.
class ExchangeRate(object):
    """Missing associated documentation comment in .proto file"""

    @staticmethod
    def Subscribe(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_stream(request, target, '/ExchangeRate/Subscribe',
            exchange__rate__pb2.Condition.SerializeToString,
            exchange__rate__pb2.CurrencyNotification.FromString,
            options, channel_credentials,
            call_credentials, compression, wait_for_ready, timeout, metadata)
