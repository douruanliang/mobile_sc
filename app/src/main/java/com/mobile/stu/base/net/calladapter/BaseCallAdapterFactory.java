package com.mobile.stu.base.net.calladapter;
import com.mobile.stu.base.net.callback.ICallback;
import com.mobile.stu.base.net.exception.ApiException;
import com.mobile.stu.base.net.model.IResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * author: dourl
 * created on: 2018/8/6 下午4:44
 * description:
 */
public class BaseCallAdapterFactory extends CallAdapter.Factory {
    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != BaseCall.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException(
                    "CompactCall must have generic type (e.g., CompactCall<ResponseBody>)");
        }
        Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Executor callbackExecutor = retrofit.callbackExecutor();
        return new ErrorHandlingCallAdapter<>(responseType, callbackExecutor);
    }


    @SuppressWarnings("unchecked")
    private static final class ErrorHandlingCallAdapter<R> implements CallAdapter<R, BaseCall<R>> {
        private final Type responseType;
        private final Executor callbackExecutor;

        ErrorHandlingCallAdapter(Type responseType, Executor callbackExecutor) {
            this.responseType = responseType;
            this.callbackExecutor = callbackExecutor;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public BaseCall<R> adapt(@NonNull Call<R> call) {
            return new InternalCallAdapter<>(call, callbackExecutor);
        }

    }
    public interface ImCall<T> extends BaseCall<T> {

        Call<T> getCall();

    }


    /**
     * Adapts a {@link Call} to {@link BaseCall}.
     */
    @SuppressWarnings("CloneDoesntCallSuperClone")
    private static class InternalCallAdapter<T> implements BaseCall<T> {
        private final Call<T> call;
        private final Executor callbackExecutor;

        InternalCallAdapter(Call<T> call, Executor callbackExecutor) {
            this.call = call;
            this.callbackExecutor = callbackExecutor;
        }

        @Override
        public void cancel() {
            call.cancel();
        }

        @Override
        public void enqueue(@Nullable ICallback<T> callback) {
            enqueue(callback, null);
        }


        @Override
        public void enqueue(@Nullable final ICallback<T> callback, final Lifecycle lifecycle) {
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(@NonNull Call<T> call, @NonNull final Response<T> response) {
                    if (lifecycle != null) {
                        if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
                            return;
                        }
                    }
                    if (callbackExecutor != null) {
                        callbackExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                handleResponse(response, callback);
                                if (callback != null) {
                                    callback.onFinish();
                                }
                            }
                        });
                    } else {
                        handleResponse(response, callback);
                        if (callback != null) {
                            callback.onFinish();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<T> call, @NonNull final Throwable t) {
                    if (callbackExecutor != null) {
                        callbackExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                int code = getCode(t);
                                if (callback != null) {
                                    callback.onFail(code, null, t);
                                    callback.onFinish();
                                }
                            }
                        });
                    } else {
                        int code = getCode(t);
                        if (callback != null) {
                            callback.onFail(code, null, t);
                            callback.onFinish();
                        }
                    }
                }
            });
        }

        private int getCode(Throwable t) {
            int code = ICallback.OTHER_STATUS_CODE;
            if (t instanceof IOException) {
                code = ICallback.NO_NETWORK_STATUS_CODE;
            }
            return code;
        }

        @Override
        public Response<T> execute() throws IOException {
            return call.execute();
        }

        private void handleResponse(Response<T> response, @Nullable ICallback<T> callback) {
            if (response.isSuccessful()) {
                T body = response.body();
                  //自定义数据响应
                if (body instanceof IResponse) {
                    if (((IResponse) body).isSucceeded()) {
                        if (callback != null) {
                            callback.onSuccess(body);
                        }
                    } else {
                        if (callback != null) {
                            callback.onFail(((IResponse) body).getErrorCode(), body,
                                    new ApiException(((IResponse) body).getErrorCode(), ((IResponse) body).getErrorMessage()));
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onSuccess(body);
                    }
                }
            } else {
                if (callback != null) {
                    callback.onFail(response.code(), response.body(), new ApiException(response.code(), response.message()));
                }
            }
        }

        @Override
        public BaseCall<T> clone() {
            return new InternalCallAdapter<>(call.clone(), callbackExecutor);
        }
    }
}
