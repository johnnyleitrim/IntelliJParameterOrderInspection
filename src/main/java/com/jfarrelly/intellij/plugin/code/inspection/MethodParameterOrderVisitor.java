package com.jfarrelly.intellij.plugin.code.inspection;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiCall;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiParameter;

public class MethodParameterOrderVisitor extends JavaElementVisitor {

  private static final String DESCRIPTION_TEMPLATE =
      "Argument '%s' in different order than '%s' parameter on method '%s'";
  private static final int MIN_ARGUMENTS = 2;

  private final ProblemsHolder holder;
  private final int minIncorrectParameters;

  public MethodParameterOrderVisitor(ProblemsHolder holder, int minIncorrectParameters) {
    this.holder = holder;
    this.minIncorrectParameters = minIncorrectParameters;
  }

  @Override
  public void visitMethodCallExpression(PsiMethodCallExpression methodCallExpression) {
    super.visitMethodCallExpression(methodCallExpression);

    if (methodCallExpression.getArgumentList().getExpressionCount() < MIN_ARGUMENTS) {
      return;
    }

    List<String> argumentNames =
        new ArrayList<>(methodCallExpression.getArgumentList().getExpressionCount());
    for (PsiExpression argument : methodCallExpression.getArgumentList().getExpressions()) {
      argumentNames.add(getArgumentName(argument));
    }

    if (hasNonNullElements(argumentNames, MIN_ARGUMENTS)) {
      PsiMethod calledMethod = methodCallExpression.resolveMethod();
      if (calledMethod != null) {
        int nParametersToCheck =
            Math.min(calledMethod.getParameterList().getParametersCount(), argumentNames.size());
        List<Pair<PsiElement, String>> problems = new ArrayList<>(nParametersToCheck);
        for (int parameterPosition = 0;
             parameterPosition < nParametersToCheck;
             parameterPosition++) {
          PsiParameter parameter = calledMethod.getParameterList().getParameter(parameterPosition);
          if (parameter != null) {
            Pair<PsiElement, String> problem =
                checkParameterPosition(
                    argumentNames, parameterPosition, parameter, methodCallExpression);
            if (problem != null) {
              problems.add(problem);
            }
          }
        }
        if (problems.size() >= minIncorrectParameters) {
          for (Pair<PsiElement, String> problem : problems) {
            holder.registerProblem(problem.first, problem.second);
          }
        }
      }
    }
  }

  @Nullable
  private static Pair<PsiElement, String> checkParameterPosition(
      @NotNull List<String> argumentNames,
      int parameterPosition,
      @NotNull PsiParameter parameter,
      @NotNull PsiMethodCallExpression methodCallExpression) {
    String parameterName = toLower(parameter);
    if (!Objects.equals(parameterName, argumentNames.get(parameterPosition))) {
      int argumentPosition = argumentNames.indexOf(parameterName);
      if (argumentPosition != -1 && argumentPosition != parameterPosition) {
        PsiExpression argument =
            methodCallExpression.getArgumentList().getExpressions()[argumentPosition];
        return Pair.create(
            argument,
            String.format(
                DESCRIPTION_TEMPLATE,
                argument.getText(),
                parameter.getName(),
                methodCallExpression.getMethodExpression().getText()));
      }
    }
    return null;
  }

  @Nullable
  private static String getArgumentName(PsiExpression argument) {
    if (argument.getReference() != null) {
      return toLower(argument.getReference().getElement());
    } else if (argument instanceof PsiMethodCallExpression) {
      PsiMethod method = ((PsiCall) argument).resolveMethod();
      return toAttributeName(method);
    }
    return null;
  }

  @Nullable
  private static String toLower(@Nullable String text) {
    return text == null ? null : text.toLowerCase(Locale.ROOT);
  }

  @Nullable
  private static String toLower(@NotNull PsiElement element) {
    if (element instanceof PsiNamedElement) {
      return toLower(((PsiNamedElement) element).getName());
    }
    return toLower(element.getText());
  }

  @Nullable
  private static String toAttributeName(@Nullable PsiMethod method) {
    if (method != null) {
      String methodName = method.getName();
      if (methodName.length() > 3 && methodName.startsWith("get")) {
        return toLower(method.getName().substring(3));
      }
    }
    return null;
  }

  private static boolean hasNonNullElements(@NotNull List<String> list, int minCount) {
    int nonNullCount = 0;
    for (String str : list) {
      if (str != null) {
        nonNullCount++;
      }
    }
    return nonNullCount >= minCount;
  }
}
