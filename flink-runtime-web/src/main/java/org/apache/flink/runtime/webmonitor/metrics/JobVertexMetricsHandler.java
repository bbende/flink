/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.flink.runtime.webmonitor.metrics;

import java.util.Map;

/**
 * Request handler that returns for a given task a list of all available metrics or the values for a set of metrics.
 *
 * If the query parameters do not contain a "get" parameter the list of all metrics is returned.
 * {@code {"available": [ { "name" : "X", "id" : "X" } ] } }
 *
 * If the query parameters do contain a "get" parameter a comma-separate list of metric names is expected as a value.
 * {@code /get?X,Y}
 * The handler will then return a list containing the values of the requested metrics.
 * {@code [ { "id" : "X", "value" : "S" }, { "id" : "Y", "value" : "T" } ] }
 */
public class JobVertexMetricsHandler extends AbstractMetricsHandler {
	public static final String PARAMETER_VERTEX_ID = "vertexid";

	public JobVertexMetricsHandler(MetricFetcher fetcher) {
		super(fetcher);
	}

	@Override
	protected Map<String, Object> getMapFor(Map<String, String> pathParams, MetricStore metrics) {
		MetricStore.JobMetricStore job = metrics.jobs.get(pathParams.get(JobMetricsHandler.PARAMETER_JOB_ID));
		if (job == null) {
			return null;
		} else {
			MetricStore.TaskMetricStore task = job.tasks.get(pathParams.get(PARAMETER_VERTEX_ID));
			if (task == null) {
				return null;
			} else {
				return task.metrics;
			}
		}
	}
}
