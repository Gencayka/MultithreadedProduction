# MultithreadedProduction
Результат выполнения упражнения по многопоточному программированию.
# Задание
Написать программу, реализовывающую схему производства - потребления ресурсов.

Существуют производители и потребители. Производитель производит 1 единицу ресурса за время X и помещает на склад, потребитель забирает со склада и потребляет одну единицу ресурса за время Y. Каждому производителю и потребителю выделяется отдельный поток.

Если склад пустой, потребители ждут, пока на нем не появятся ресурсы. Если склад переполнен, производители ждут, пока место не освободится.

В программе нельзя использовать java.util.concurrent.
# Реализация
Версия JDK 15.0.2, для сборки проекта используется Gradle.

В основном потоке создаются и запускаются потребители и производители, а также создается склад. Если программа запущена с ограничением по времени, то в основном потоке также останавливаются производители и потребители по истечении времени.

Производитель в бесконечном цикле вызывает метод объекта склада addResource (потребитель вызывет метод consumeResource), затем поток производителя засыпает на заданное время.

Объект склада содержит два поля-объекта синхронизации - один для потребителей, другой для производителей. При вызове метода addResource производитель входит в синхронизованную секцию для потребителей, проверяет, есть ли место на складе, и, если нет, переходит в режим ожидания. При выходе из него потребитель вновь проверяет загруженность склада и вновь уходит в режим ожидания, если склад все еще переполнен. Если на складе есть место, потребитель добавляет в него произведенный ресурс, затем выходит из синхронизованной секции для производителей и захватывает объект синхронизации для потребителей, чтобы уведомить потребитель о производстве ресурса. Потребители и метод consumeResource работают аналогично.

Каждому производителю, потребителю и ресурсу присваивается уникальный ID для логирования работы программы.

Некоторые параметры программы вынесены в файл data.properties в папке resources. Если какое-либо из значений, данных в файле не валидно, вместо него береться захардкоженное дефолтное значение. Данные из файла data.properties считываются и проверяются на валидность при запуске программы. Считанные (либо дефолтные, если значения не валидны или файл не удалось прочесть) значения записываются в поля объекта синглтон-класса Settings и далее считываются из него.