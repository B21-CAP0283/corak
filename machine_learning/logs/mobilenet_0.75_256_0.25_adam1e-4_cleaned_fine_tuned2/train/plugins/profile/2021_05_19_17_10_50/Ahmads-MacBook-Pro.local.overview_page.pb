?	??Mb??@??Mb??@!??Mb??@	?e???@?e???@!?e???@"e
=type.googleapis.com/tensorflow.profiler.PerGenericStepDetails$??Mb??@??Q???A?A`??f?@Y}?5^??A@*	    ?%A2~
GIterator::Model::MaxIntraOpParallelism::Prefetch::FlatMap[0]::GeneratorX9??vT?@!I??T?qV@)X9??vT?@1I??T?qV@:Preprocessing2F
Iterator::Model??ʡE?A@!Ɩ??~@)??ʡE?A@1Ɩ??~@:Preprocessing2g
0Iterator::Model::MaxIntraOpParallelism::Prefetch?p=
דA@!y????h@)?p=
דA@1y????h@:Preprocessing2p
9Iterator::Model::MaxIntraOpParallelism::Prefetch::FlatMap?G?zT?@!?z<?qV@)????Mb`?1??3?:Preprocessing:?
]Enqueuing data: you may want to combine small input data chunks into fewer but larger chunks.
?Data preprocessing: you may increase num_parallel_calls in <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#map" target="_blank">Dataset map()</a> or preprocess the data OFFLINE.
?Reading data from files in advance: you may tune parameters in the following tf.data API (<a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#prefetch" target="_blank">prefetch size</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#interleave" target="_blank">interleave cycle_length</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/TFRecordDataset#class_tfrecorddataset" target="_blank">reader buffer_size</a>)
?Reading data from files on demand: you should read data IN ADVANCE using the following tf.data API (<a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#prefetch" target="_blank">prefetch</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#interleave" target="_blank">interleave</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/TFRecordDataset#class_tfrecorddataset" target="_blank">reader buffer</a>)
?Other data reading or processing: you may consider using the <a href="https://www.tensorflow.org/programmers_guide/datasets" target="_blank">tf.data API</a> (if you are not using it now)?
:type.googleapis.com/tensorflow.profiler.BottleneckAnalysis?
both?Your program is MODERATELY input-bound because 5.4% of the total step time sampled is waiting for input. Therefore, you would need to reduce both the input time and other time.no*no9?e???@I???6??W@Zno>Look at Section 3 for the breakdown of input time on the host.B?
@type.googleapis.com/tensorflow.profiler.GenericStepTimeBreakdown?
	??Q?????Q???!??Q???      ??!       "      ??!       *      ??!       2	?A`??f?@?A`??f?@!?A`??f?@:      ??!       B      ??!       J	}?5^??A@}?5^??A@!}?5^??A@R      ??!       Z	}?5^??A@}?5^??A@!}?5^??A@b      ??!       JCPU_ONLYY?e???@b q???6??W@Y      Y@q??p????"?
both?Your program is MODERATELY input-bound because 5.4% of the total step time sampled is waiting for input. Therefore, you would need to reduce both the input time and other time.b
`input_pipeline_analyzer (especially Section 3 for the breakdown of input operations on the Host)Q
Otf_data_bottleneck_analysis (find the bottleneck in the tf.data input pipeline)m
ktrace_viewer (look at the activities on the timeline of each Host Thread near the bottom of the trace view)"T
Rtensorflow_stats (identify the time-consuming operations executed on the CPU_ONLY)"Z
Xtrace_viewer (look at the activities on the timeline of each CPU_ONLY in the trace view)*?
?<a href="https://www.tensorflow.org/guide/data_performance_analysis" target="_blank">Analyze tf.data performance with the TF Profiler</a>*y
w<a href="https://www.tensorflow.org/guide/data_performance" target="_blank">Better performance with the tf.data API</a>2M
=type.googleapis.com/tensorflow.profiler.GenericRecommendation
nono2no:
Refer to the TF2 Profiler FAQ2"CPU: B 