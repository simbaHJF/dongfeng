desc "dongfeng-common"
task :common do
    sh 'cd dongfeng-common && mvn clean install -Dmaven.test.skip=true'
end



desc "donfeng-center prod compile"
task :center_compile_prod => [:common] do
    sh 'cd dongfeng-center && mvn clean package -Pprod -Dmaven.test.skip=true'
end

desc "donfeng-center test compile"
task :center_compile_test => [:common] do
    sh 'cd dongfeng-center && mvn clean package -Ptest -Dmaven.test.skip=true'
end

desc "donfeng-executor prod compile"
task :executor_compile_prod => [:common] do
    sh 'cd dongfeng-executor && mvn clean package -Pprod -Dmaven.test.skip=true'
end

desc "donfeng-executor test compile"
task :executor_compile_test => [:common] do
    sh 'cd dongfeng-executor && mvn clean package -Ptest -Dmaven.test.skip=true'
end


desc "center prod upload"
task :center_prod_upload => [:center_compile_prod] do
    sh 'sh upload.sh v-bosspixiu-01 /data/app/dongfeng dongfeng-center-0.0.1-SNAPSHOT.jar dongfeng-center/target'
end



desc "app prod upload"
task :app_prod_upload => [:app_prod_compile] do
    sh 'sh upload.sh v-bosspixiu-01 /data/app/jinnang jinnang-app-1.0-SNAPSHOT-jar-with-dependencies.jar jinnang-app/target'
end





desc "task prod upload"
task :task_prod_upload => [:task_prod_compile] do
    sh 'sh upload.sh v-bosspixiu-01 /data/app/jinnang jinnang-task-1.0-SNAPSHOT-jar-with-dependencies.jar jinnang-task/target'
end
