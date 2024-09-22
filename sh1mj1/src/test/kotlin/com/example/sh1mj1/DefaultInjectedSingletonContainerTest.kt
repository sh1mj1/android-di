package com.example.sh1mj1

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.ComponentKey
import com.example.sh1mj1.component.InjectedComponent
import com.example.sh1mj1.component.InjectedSingletonContainer
import com.example.sh1mj1.container.DefaultInjectedSingletonContainer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultInjectedSingletonContainerTest {
    private lateinit var container: InjectedSingletonContainer

    @BeforeEach
    fun setUp() {
        container = DefaultInjectedSingletonContainer.instance
    }

    @AfterEach
    fun tearDown() {
        container.clear()
    }

    interface StubRepo

    class Default1StubRepo : StubRepo

    class Default2StubRepo : StubRepo

    @Test
    fun `같은 타입이며 같은 Qualfier 인 컴포넌트를 컨테이너에 넣을 때 예외를 던진다`() {
        // given
        val component1 =
            InjectedComponent.InjectedSingletonComponent(
                StubRepo::class,
                Default1StubRepo(),
                Qualifier("1"),
            )

        val component2 =
            InjectedComponent.InjectedSingletonComponent(
                StubRepo::class,
                Default2StubRepo(),
                Qualifier("1"),
            )

        // when
        container.add(component1)

        // then
        shouldThrow<IllegalStateException> {
            container.add(component2)
        }
    }

    @Test
    fun `하나의 타입 컴포넌트를 컨테이너에 넣고 찾는다`() {
        // given
        val component =
            InjectedComponent.InjectedSingletonComponent(
                StubRepo::class,
                Default1StubRepo(),
            )

        // when
        container.add(component)

        // then
        val stubRepo =
            container.find(
                ComponentKey.of(
                    StubRepo::class,
                    null,
                ),
            )
        stubRepo.shouldBeInstanceOf<Default1StubRepo>()
    }

    @Test
    fun `같은 타입이고 하나에만 Qualifier 가 붙어있는 컴포넌트를 컨테이너에 넣고 클래스로만 찾을 때 예외를 던진다`() {
        // given
        val component1 =
            InjectedComponent.InjectedSingletonComponent(
                StubRepo::class,
                Default1StubRepo(),
            )

        val component2 =
            InjectedComponent.InjectedSingletonComponent(
                StubRepo::class,
                Default2StubRepo(),
                Qualifier("1"),
            )

        // when
        container.add(component1)
        container.add(component2)

        // then
        val foundComponent1 =
            container.find(
                ComponentKey.of(
                    StubRepo::class,
                    null,
                ),
            )
        foundComponent1.shouldBeInstanceOf<Default1StubRepo>()

        val foundComponent2 =
            container.find(
                ComponentKey.of(
                    StubRepo::class,
                    Qualifier("1"),
                ),
            )
        foundComponent2.shouldBeInstanceOf<Default2StubRepo>()
    }
}
