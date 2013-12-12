/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.documentsui;

<<<<<<< HEAD
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import com.google.android.collect.Maps;

=======
import android.os.AsyncTask;

import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import com.google.android.collect.Lists;
import com.google.android.collect.Maps;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

public class ProviderExecutor extends Thread implements Executor {

    @GuardedBy("sExecutors")
    private static HashMap<String, ProviderExecutor> sExecutors = Maps.newHashMap();

<<<<<<< HEAD
    public static Executor forAuthority(String authority) {
=======
    public static ProviderExecutor forAuthority(String authority) {
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
        synchronized (sExecutors) {
            ProviderExecutor executor = sExecutors.get(authority);
            if (executor == null) {
                executor = new ProviderExecutor();
                executor.setName("ProviderExecutor: " + authority);
                executor.start();
                sExecutors.put(authority, executor);
            }
            return executor;
        }
    }

<<<<<<< HEAD
    private final LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<Runnable>();

    @Override
    public void execute(Runnable command) {
=======
    public interface Preemptable {
        void preempt();
    }

    private final LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<Runnable>();

    private final ArrayList<WeakReference<Preemptable>> mPreemptable = Lists.newArrayList();

    private void preempt() {
        synchronized (mPreemptable) {
            int count = 0;
            for (WeakReference<Preemptable> ref : mPreemptable) {
                final Preemptable p = ref.get();
                if (p != null) {
                    count++;
                    p.preempt();
                }
            }
            mPreemptable.clear();
        }
    }

    /**
     * Execute the given task. If given task is not {@link Preemptable}, it will
     * preempt all outstanding preemptable tasks.
     */
    public <P> void execute(AsyncTask<P, ?, ?> task, P... params) {
        if (task instanceof Preemptable) {
            synchronized (mPreemptable) {
                mPreemptable.add(new WeakReference<Preemptable>((Preemptable) task));
            }
            task.executeOnExecutor(mNonPreemptingExecutor, params);
        } else {
            task.executeOnExecutor(this, params);
        }
    }

    private Executor mNonPreemptingExecutor = new Executor() {
        @Override
        public void execute(Runnable command) {
            Preconditions.checkNotNull(command);
            mQueue.add(command);
        }
    };

    @Override
    public void execute(Runnable command) {
        preempt();
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
        Preconditions.checkNotNull(command);
        mQueue.add(command);
    }

    @Override
    public void run() {
        while (true) {
            try {
                final Runnable command = mQueue.take();
                command.run();
            } catch (InterruptedException e) {
                // That was weird; let's go look for more tasks.
            }
        }
    }
}
