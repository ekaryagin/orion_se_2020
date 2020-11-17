package com.mer.spring.person;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProxyExample {



    interface ShopService {
        String getShopName();
        List<String> getItems();
    }

    static class ShopImpl  implements ShopService{
        String name;
        List<String> items;

        public ShopImpl(String name, List<String> items) {
            this.name = name;
            this.items = items;
        }

        public String getShopName() {
            return name;
        }

        public List<String> getItems() {
            return items;
        }
    }




    public static void main(String[] args) {


        ShopService shop = new ShopImpl("Пятерочка", Arrays.asList("Молоко", "Хлеб", "Сигареты"));


        final ShopService object = getShopService(shop);

        System.out.println(object.getItems());
        System.out.println(object.getShopName());


    }

    private static ShopService getShopService(ShopService shop) {

        final ShopService object = (ShopService)Proxy.newProxyInstance(ProxyExample.class.getClassLoader(), new Class[]{ShopService.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                if (method.getName().equals("getItems")) {
                    return shop.getItems()
                            .stream()
                            .filter(i -> !i.equals("Сигареты"))
                            .collect(Collectors.toList());
                }

                if (method.getName().equals("getShopName")) {
                    return shop.getShopName().toUpperCase();
                }

                return method.invoke(shop);

            }
        });
        return object;
    }
}
