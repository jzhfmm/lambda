package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.BifunctorLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.monad.transformer.builtin.EitherT.eitherT;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class EitherTTest {

    @TestTraits({
            FunctorLaws.class,
            ApplicativeLaws.class,
            MonadLaws.class,
            BifunctorLaws.class,
            MonadRecLaws.class})
    public Subjects<EitherT<Identity<?>, String, Integer>> testSubject() {
        return subjects(eitherT(new Identity<>(left("foo"))), eitherT(new Identity<>(right(1))));
    }

    @Test
    public void lazyZip() {
        assertEquals(eitherT(just(right(2))),
                     eitherT(just(right(1))).lazyZip(lazy(eitherT(just(right(x -> x + 1))))).value());
        assertEquals(eitherT(nothing()),
                     eitherT(nothing()).lazyZip(lazy(() -> {
                         throw new AssertionError();
                     })).value());
    }

    @Test
    public void staticPure() {
        EitherT<Identity<?>, String, Integer> eitherT = EitherT.<Identity<?>, String>pureEitherT(pureIdentity())
                .apply(1);
        assertEquals(eitherT(new Identity<>(right(1))), eitherT);
    }
}