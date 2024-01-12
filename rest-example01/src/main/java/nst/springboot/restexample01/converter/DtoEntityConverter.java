package nst.springboot.restexample01.converter;

public interface DtoEntityConverter<T, E> {
    T toDto(E e);
    E toEntity(T t);
}
