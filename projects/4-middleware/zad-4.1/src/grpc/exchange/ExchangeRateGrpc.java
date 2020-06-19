package grpc.exchange;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.29.0)",
    comments = "Source: exchange_rate.proto")
public final class ExchangeRateGrpc {

  private ExchangeRateGrpc() {}

  public static final String SERVICE_NAME = "ExchangeRate";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc.exchange.ExchangeRateOuterClass.Condition,
      grpc.exchange.ExchangeRateOuterClass.CurrencyNotification> getSubscribeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Subscribe",
      requestType = grpc.exchange.ExchangeRateOuterClass.Condition.class,
      responseType = grpc.exchange.ExchangeRateOuterClass.CurrencyNotification.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<grpc.exchange.ExchangeRateOuterClass.Condition,
      grpc.exchange.ExchangeRateOuterClass.CurrencyNotification> getSubscribeMethod() {
    io.grpc.MethodDescriptor<grpc.exchange.ExchangeRateOuterClass.Condition, grpc.exchange.ExchangeRateOuterClass.CurrencyNotification> getSubscribeMethod;
    if ((getSubscribeMethod = ExchangeRateGrpc.getSubscribeMethod) == null) {
      synchronized (ExchangeRateGrpc.class) {
        if ((getSubscribeMethod = ExchangeRateGrpc.getSubscribeMethod) == null) {
          ExchangeRateGrpc.getSubscribeMethod = getSubscribeMethod =
              io.grpc.MethodDescriptor.<grpc.exchange.ExchangeRateOuterClass.Condition, grpc.exchange.ExchangeRateOuterClass.CurrencyNotification>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Subscribe"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.exchange.ExchangeRateOuterClass.Condition.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.exchange.ExchangeRateOuterClass.CurrencyNotification.getDefaultInstance()))
              .setSchemaDescriptor(new ExchangeRateMethodDescriptorSupplier("Subscribe"))
              .build();
        }
      }
    }
    return getSubscribeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ExchangeRateStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ExchangeRateStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ExchangeRateStub>() {
        @java.lang.Override
        public ExchangeRateStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ExchangeRateStub(channel, callOptions);
        }
      };
    return ExchangeRateStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ExchangeRateBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ExchangeRateBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ExchangeRateBlockingStub>() {
        @java.lang.Override
        public ExchangeRateBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ExchangeRateBlockingStub(channel, callOptions);
        }
      };
    return ExchangeRateBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ExchangeRateFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ExchangeRateFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ExchangeRateFutureStub>() {
        @java.lang.Override
        public ExchangeRateFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ExchangeRateFutureStub(channel, callOptions);
        }
      };
    return ExchangeRateFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class ExchangeRateImplBase implements io.grpc.BindableService {

    /**
     */
    public void subscribe(grpc.exchange.ExchangeRateOuterClass.Condition request,
        io.grpc.stub.StreamObserver<grpc.exchange.ExchangeRateOuterClass.CurrencyNotification> responseObserver) {
      asyncUnimplementedUnaryCall(getSubscribeMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSubscribeMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                grpc.exchange.ExchangeRateOuterClass.Condition,
                grpc.exchange.ExchangeRateOuterClass.CurrencyNotification>(
                  this, METHODID_SUBSCRIBE)))
          .build();
    }
  }

  /**
   */
  public static final class ExchangeRateStub extends io.grpc.stub.AbstractAsyncStub<ExchangeRateStub> {
    private ExchangeRateStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ExchangeRateStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ExchangeRateStub(channel, callOptions);
    }

    /**
     */
    public void subscribe(grpc.exchange.ExchangeRateOuterClass.Condition request,
        io.grpc.stub.StreamObserver<grpc.exchange.ExchangeRateOuterClass.CurrencyNotification> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getSubscribeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ExchangeRateBlockingStub extends io.grpc.stub.AbstractBlockingStub<ExchangeRateBlockingStub> {
    private ExchangeRateBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ExchangeRateBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ExchangeRateBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<grpc.exchange.ExchangeRateOuterClass.CurrencyNotification> subscribe(
        grpc.exchange.ExchangeRateOuterClass.Condition request) {
      return blockingServerStreamingCall(
          getChannel(), getSubscribeMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ExchangeRateFutureStub extends io.grpc.stub.AbstractFutureStub<ExchangeRateFutureStub> {
    private ExchangeRateFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ExchangeRateFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ExchangeRateFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_SUBSCRIBE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ExchangeRateImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ExchangeRateImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SUBSCRIBE:
          serviceImpl.subscribe((grpc.exchange.ExchangeRateOuterClass.Condition) request,
              (io.grpc.stub.StreamObserver<grpc.exchange.ExchangeRateOuterClass.CurrencyNotification>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ExchangeRateBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ExchangeRateBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.exchange.ExchangeRateOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ExchangeRate");
    }
  }

  private static final class ExchangeRateFileDescriptorSupplier
      extends ExchangeRateBaseDescriptorSupplier {
    ExchangeRateFileDescriptorSupplier() {}
  }

  private static final class ExchangeRateMethodDescriptorSupplier
      extends ExchangeRateBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ExchangeRateMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ExchangeRateGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ExchangeRateFileDescriptorSupplier())
              .addMethod(getSubscribeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
